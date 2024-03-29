import { Subject } from 'rxjs';
import { Observable } from 'rxjs';
import { EventEmitter } from '@angular/core';

import { Deferred, getDeepFromObject } from './helpers';
import { Column } from './data-set/column';
import { Row } from './data-set/row';
import { DataSet } from './data-set/data-set';
import { DataSource } from './data-source/data-source';
import { CustomRow } from './data-set/custom-row';

export class Grid {

  createFormShown: boolean = false;

  source: DataSource;
  settings: any;
  dataSet: DataSet;

  onSelectRowSource = new Subject<any>();
  private rowData: Array<CustomRow> = [];

  constructor(source: DataSource, settings: any) {
    this.setSettings(settings);
    this.setSource(source);
  }

  showActionColumn(position: string): boolean {
    return this.isCurrentActionsPosition(position) && this.isActionsVisible();
  }

  isCurrentActionsPosition(position: string): boolean {
    return position == this.getSetting('actions.position');
  }

  isActionsVisible(): boolean {
    return this.getSetting('actions.add') || this.getSetting('actions.edit') || this.getSetting('actions.delete') || this.getSetting('actions.custom').length;
  }

  isMultiSelectVisible(): boolean {
    return this.getSetting('selectMode') === 'multi';
  }

  getNewRow(): Row {
    return this.dataSet.newRow;
  }

  setSettings(settings: Object) {
    this.settings = settings;
    this.dataSet = new DataSet([], this.getSetting('columns'));

    if (this.source) {
      this.source.refresh();
    }
  }

  getDataSet(): DataSet {
    return this.dataSet;
  }

  setSource(source: DataSource) {
    this.source = this.prepareSource(source);

    this.source.onChanged().subscribe((changes: any) => this.processDataChange(changes));

    this.source.onUpdated().subscribe((data: any) => {
      const changedRow = this.dataSet.findRowByData(data);
      changedRow.setData(data);
    });
  }

  getSetting(name: string, defaultValue?: any): any {
    return getDeepFromObject(this.settings, name, defaultValue);
  }

  getColumns(): Array<Column> {
    return this.dataSet.getColumns();
  }

  getRows(): Array<Row> {
    return this.dataSet.getRows();
  }

  selectRow(row: Row) {
    this.dataSet.selectRow(row);
  }

  multipleSelectRow(row: Row) {
    this.dataSet.multipleSelectRow(row);
  }

  onSelectRow(): Observable<any> {
    return this.onSelectRowSource.asObservable();
  }

  edit(row: Row) {
    row.isInEditing = true;
  }

  create(row: Row, confirmEmitter: EventEmitter<any>) {

    const deferred = new Deferred();
    deferred.promise.then((newData) => {
      newData = newData ? newData : row.getNewData();
      if (deferred.resolve.skipAdd) {
        this.createFormShown = false;
      } else {
        this.source.prepend(newData).then(() => {
          this.createFormShown = false;
          this.dataSet.createNewRow();
        });
      }
    }).catch((err) => {
      // doing nothing
    });

    if (this.getSetting('add.confirmCreate')) {
      confirmEmitter.emit({
        newData: row.getNewData(),
        source: this.source,
        confirm: deferred,
      });
    } else {
      deferred.resolve();
    }
  }

  save(row: Row, confirmEmitter: EventEmitter<any>) {

    const deferred = new Deferred();
    deferred.promise.then((newData) => {
      newData = newData ? newData : row.getNewData();
      if (deferred.resolve.skipEdit) {
        row.isInEditing = false;
      } else {
        this.source.update(row.getData(), newData).then(() => {
          row.isInEditing = false;
        });
      }
    }).catch((err) => {
      // doing nothing
    });

    if (this.getSetting('edit.confirmSave')) {
      confirmEmitter.emit({
        data: row.getData(),
        newData: row.getNewData(),
        source: this.source,
        confirm: deferred,
      });
    } else {
      deferred.resolve();
    }
  }

  delete(row: Row, confirmEmitter: EventEmitter<any>) {

    const deferred = new Deferred();
    deferred.promise.then(() => {
      this.source.remove(row.getData());
    }).catch((err) => {
      // doing nothing
    });

    if (this.getSetting('delete.confirmDelete')) {
      confirmEmitter.emit({
        data: row.getData(),
        source: this.source,
        confirm: deferred,
      });
    } else {
      deferred.resolve();
    }
  }

  processDataChange(changes: any) {
    if (this.shouldProcessChange(changes)) {
      this.dataSet.setData(changes['elements']);
      if (this.getSetting('selectMode') !== 'multi' && this.getSetting('selectMode') !== 'radio') {
        const row = this.determineRowToSelect(changes);

        if (row) {
          this.onSelectRowSource.next(row);
        }
      } else {
        if (this.rowData.length > 0) {
          this.getDataSet().getRows().forEach((r) => {
            let isDataAvaiable = this.rowData.find((preSelectedRow) => preSelectedRow.getData()['tableId'] === r.getData()['tableId']);
            r.isSelected = isDataAvaiable.isChecked;
          });
        }
      }
    }
  }

  shouldProcessChange(changes: any): boolean {
    if (['filter', 'sort', 'page', 'remove', 'refresh', 'load', 'paging'].indexOf(changes['action']) !== -1) {
      return true;
    } else if (['prepend', 'append'].indexOf(changes['action']) !== -1 && !this.getSetting('pager.display')) {
      return true;
    }

    return false;
  }

  // TODO: move to selectable? Separate directive
  determineRowToSelect(changes: any): Row {

    if (['load', 'page', 'filter', 'sort', 'refresh'].indexOf(changes['action']) !== -1) {
      return this.dataSet.select();
    }
    if (changes['action'] === 'remove') {
      if (changes['elements'].length === 0) {
        // we have to store which one to select as the data will be reloaded
        this.dataSet.willSelectLastRow();
      } else {
        return this.dataSet.selectPreviousRow();
      }
    }
    if (changes['action'] === 'append') {
      // we have to store which one to select as the data will be reloaded
      this.dataSet.willSelectLastRow();
    }
    if (changes['action'] === 'add') {
      return this.dataSet.selectFirstRow();
    }
    if (changes['action'] === 'update') {
      return this.dataSet.selectFirstRow();
    }
    if (changes['action'] === 'prepend') {
      // we have to store which one to select as the data will be reloaded
      this.dataSet.willSelectFirstRow();
    }
    return null;
  }

  prepareSource(source: any): DataSource {
    const initialSource: any = this.getInitialSort();
    if (initialSource && initialSource['field'] && initialSource['direction']) {
      source.setSort([initialSource], false);
    }
    if (this.getSetting('pager.display') === true) {
      source.setPaging(1, this.getSetting('pager.perPage'), false);
    }

    source.refresh();
    return source;
  }

  getInitialSort() {
    const sortConf: any = {};
    this.getColumns().forEach((column: Column) => {
      if (column.isSortable && column.defaultSortDirection) {
        sortConf['field'] = column.id;
        sortConf['direction'] = column.defaultSortDirection;
        sortConf['compare'] = column.getCompareFunction();
      }
    });
    return sortConf;
  }

  getSelectedRows(): Array<any> {
    return this.dataSet.getRows()
      .filter(r => r.isSelected);
  }

  selectAllRows(status: any) {
    this.dataSet.getRows()
      .forEach(r => { if (r.isEditable) r.isSelected = status });
  }

  getFirstRow(): Row {
    return this.dataSet.getFirstRow();
  }

  getLastRow(): Row {
    return this.dataSet.getLastRow();
  }

  /**Below method to store Preselected and selected values 
   *  in SelectedRowData property
  */

  setSelectedRowData(row: Row) {
    let isAvailable = this.getSelectedRowDataIndex(row.getData()['tableId']);
    this.updateSelectedRowData(isAvailable, row.isSelected);
  }

  setSelectedGridRowData() {
    let gridRowDataSet = this.getSelectedRows();
    gridRowDataSet.forEach((row) => {
      let isAvailable = this.rowData.findIndex((r) => (r.getData()['tableId'] === row.getData()['tableId']) && (!r.isChecked));
      this.updateSelectedRowData(isAvailable, row.isSelected);
    });
  }

  removeSelectedRowData(row: Row) {
    let isAvailable = this.getSelectedRowDataIndex(row.getData()['tableId']);
    this.updateSelectedRowData(isAvailable, false);
  }

  removeSelectedGridRowData() {
    this.dataSet.getRows().forEach((row) => {
      this.removeSelectedRowData(row);
    });
  }

  loadRowData(elementType: string) {
    this.rowData = [];
    let data = this.source['data'];
    if (elementType === 'multi' && data.length > 0) {
      data.forEach((value) => {
        value.isSelected = (value['isSelected'] !== undefined) ? (value['isSelected']) : false;
        this.pushSelectedRowData(value, value.isSelected);
      });
    } else if (elementType === 'radio' && data.length > 0) {
      let findSingleRowIndex = data.findIndex((rowData) => (rowData['isSelected']));
      data.forEach((value, index) => {
        value.isSelected = (findSingleRowIndex === index) ? true : false;
        this.pushSelectedRowData(value, value.isSelected);
      });
    }
    
  }

  checkRowIsAlreadyAdded(tableId: string): boolean {
    let isAvaiable = this.rowData.filter((r) => r.getData()['tableId'] === tableId);
    return (isAvaiable.length > 0) ? true : false;
  }

  getSelectedRowData(): Array<CustomRow> {
    return this.rowData.filter(r => r.isChecked);
  }

  isRadioSelectVisible(): boolean {
    return this.getSetting('selectMode') === 'radio';
  }

  private pushSelectedRowData(rowData: any, isChecked: boolean) {
    if (!this.checkRowIsAlreadyAdded(rowData['tableId'])) {
      let customRowObject = new CustomRow(rowData);
      customRowObject.isChecked = isChecked;
      this.rowData.push(customRowObject);
    }
  }

  private getSelectedRowDataIndex(tableId: any) {
    return this.rowData.findIndex((r) => r.getData()['tableId'] === tableId);
  }

  private updateSelectedRowData(index: number, isChecked: boolean) {
    if (index >= 0) {
      this.rowData[index].isChecked = isChecked;
    }
  }
clearRadioSelectedGridData(){
    this.rowData.forEach(r => (r.isChecked =false));
  } 
}

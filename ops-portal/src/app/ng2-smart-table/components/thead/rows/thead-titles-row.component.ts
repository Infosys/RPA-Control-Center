import {Component, Input, Output, EventEmitter, OnChanges} from '@angular/core';

import { Grid } from '../../../lib/grid';
import { DataSource } from '../../../lib/data-source/data-source';
import { Column } from "../../../lib/data-set/column";

@Component({
  selector: '[ng2-st-thead-titles-row]',
  template: `
    <th ng2-st-checkbox-select-all *ngIf="isMultiSelectVisible"
                                   [grid]="grid"
                                   [source]="source"
                                   [isAllSelected]="isAllSelected"
                                   (click)="selectAllRows.emit($event)"
                                   [hideProperty]="hideProperty">
    </th>
    
    <th *ngFor="let column of grid.getColumns()" class="ng2-smart-th {{ column.id }}" [ngClass]="column.class"
      [style.width]="column.width" >
      <ng2-st-column-title [source]="source" [column]="column" (sort)="sort.emit($event)"></ng2-st-column-title>
    </th>
    <th ng2-st-actions-title *ngIf="showActionColumnRight" [grid]="grid"></th>
    <th ng2-st-actions-title *ngIf="showActionColumnLeft" [grid]="grid"></th>
  `,
  styles: [`
    th:last-child {
    width: 78px;
    }
    `] 
})
export class TheadTitlesRowComponent implements OnChanges {

  @Input() grid: Grid;
  @Input() isAllSelected: boolean;
  @Input() source: DataSource;
  @Input() hideProperty: any;

  @Output() sort = new EventEmitter<any>();
  @Output() selectAllRows = new EventEmitter<any>();

  isMultiSelectVisible: boolean;
  showActionColumnLeft: boolean;
  showActionColumnRight: boolean;


  ngOnChanges() {
    // this.isMultiSelectVisible = this.grid.isMultiSelectVisible();
    this.isMultiSelectVisible = (this.grid.isMultiSelectVisible()) ? this.grid.isMultiSelectVisible() : this.grid.isRadioSelectVisible(); 
    this.showActionColumnLeft = this.grid.showActionColumn('left');
    this.showActionColumnRight = this.grid.showActionColumn('right');
  }

}

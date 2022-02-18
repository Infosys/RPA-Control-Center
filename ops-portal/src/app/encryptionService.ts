import { Injectable } from '@angular/core';
import * as CryptoJS from 'crypto-js';
import { environment } from '../environments/environment'
@Injectable({
  providedIn: 'root'
})

export class EncryptionService {
  constructor() { }



  encryptMode: boolean;
  textToConvert: string;
  sharedSecretPrivate: string;
  conversionOutput: string;

  encrypt(value: string) {
    this.textToConvert = value;
    this.sharedSecretPrivate =  environment.encryption_key;
    this.conversionOutput = CryptoJS.AES.encrypt(
      this.textToConvert.trim(), this.sharedSecretPrivate.trim()
    ).toString();
    return this.conversionOutput;
  }
  decrypt(value: string) {
    this.textToConvert = value;
    this.sharedSecretPrivate =  environment.encryption_key;
    this.conversionOutput = CryptoJS.AES.decrypt(
      this.textToConvert.trim(), this.sharedSecretPrivate.trim()
    ).toString(CryptoJS.enc.Utf8);
    return this.conversionOutput;
  }

  //The set method is use for encrypt the value.
  // set(value: string) {
  //   var ticks = new Date().getTime();
  //   var keys = environment.encryption_key + ticks;
  //   var key = CryptoJS.enc.Utf8.parse(keys);
  //   var iv = CryptoJS.enc.Utf8.parse(keys);
  //   var encrypted = CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(value.toString()), key,
  //     {
  //       keySize: 128 / 8,
  //       iv: iv,
  //       mode: CryptoJS.mode.CBC,
  //       padding: CryptoJS.pad.Pkcs7
  //     });

  //   return encrypted.toString();
  // }

  // //The get method is use for decrypt the value.
  // get(value: string) {
  //   var key = CryptoJS.enc.Utf8.parse(environment.encryption_key);
  //   var iv = CryptoJS.enc.Utf8.parse(environment.encryption_key);
  //   var decrypted = CryptoJS.AES.decrypt(value, key, {
  //     keySize: 128 / 8,
  //     iv: iv,
  //     mode: CryptoJS.mode.CBC,
  //     padding: CryptoJS.pad.Pkcs7
  //   });

  //   return decrypted.toString(CryptoJS.enc.Utf8);
  // }
}

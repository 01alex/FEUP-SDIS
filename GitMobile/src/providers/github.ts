import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

/*
  Generated class for the Github provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class Github {

  constructor(public http: Http) {
    console.log('Hello Github Provider');
  }

  getGitDetails() {
    
    return this.http.get('https://api.github.com/users/Guilherme-Routar').map(res => res.json());
  }

}

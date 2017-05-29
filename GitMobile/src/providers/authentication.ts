import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Auth, User } from '@ionic/cloud-angular';

import 'rxjs/add/operator/map';

/*
  Generated class for the Authentication provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class Authentication {

  constructor(public http: Http, public auth: Auth, public user: User) {
    console.log('Hello Authentication Provider');
  }

  login() {
    this.auth.login('github');
  }

  logout() {
    this.auth.logout();
  }

  getUser() {
    return this.user;
  }

  getSession() {
    return this.auth;
  }

}

import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Github } from '../../providers/github';

import { Authentication } from '../../providers/authentication';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  constructor(public navCtrl: NavController, public gitUser: Github, public auth: Authentication) {
  }

  login() {
    this.auth.login();
  }
  
}

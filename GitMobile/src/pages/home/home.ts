import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Github } from '../../providers/github';

import { Authentication } from '../../providers/authentication';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  btnDisabled = true;

  constructor(public navCtrl: NavController, public gitUser: Github, public auth: Authentication) {
  }

  login() {
    this.auth.login();
    
      //alert(this.auth.getUser().social.github.data.username);
      //this.auth.getSession().isAuthenticated()
  }
  
}

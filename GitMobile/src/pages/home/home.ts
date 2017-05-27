import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Github } from '../../providers/github';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  
  login: any;
  id: any;
  url: any;
  details: any;

  constructor(public navCtrl: NavController, public gitUser: Github) {
    this.getDetails();
  }

  getDetails() {
    this.gitUser.getGitDetails().subscribe(data => {
        this.details = data;
        this.login = data.login;
        this.id = data.id;
        this.url = data.url;
    });
  }
  
}

import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Authentication } from '../../providers/authentication';

@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class Profile {
  
  username: any;
  email: any;
  picture: any;

  constructor(public navCtrl: NavController, public auth: Authentication) {
    this.username = this.auth.getUser().social.github.data.username;
    this.email = this.auth.getUser().social.github.data.email;
    this.picture = this.auth.getUser().social.github.data.profile_picture;
  }
}

import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Auth, User } from '@ionic/cloud-angular';

/**
 * Generated class for the Login page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-login',
  templateUrl: 'login.html',
})
export class Login {

  constructor(public navCtrl: NavController, public navParams: NavParams,
              public auth: Auth, public user: User) {

                this.auth.login('github');
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad Login');
  }

}

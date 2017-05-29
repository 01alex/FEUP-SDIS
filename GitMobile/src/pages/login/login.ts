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

  info: any;
  test: any;

  constructor(public navCtrl: NavController, public navParams: NavParams,
              public auth: Auth, public user: User) {
                this.auth.login('github').then(success => {
                  alert(this.user.social.github.data.username);
                })
                this.test = "LOL";
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad Login');
  }

  getName() {
    this.info = this.user.social.github.data.full_name;
    console.log("info = " + this.info);
  }

}

import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Repos } from '../../models/repos';
import { GithubRepos } from '../../providers/github-repos';



/**
 * Generated class for the ReposDetailsPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-repos-details',
  templateUrl: 'repos-details.html',
})
export class ReposDetailsPage {
  repos : Repos;
  name : string;
  login : string;

  constructor(public navCtrl: NavController, public navParams: NavParams, private GithubRepos : GithubRepos) {
    this.name = navParams.get('name');
    this.login = navParams.get('login');
    console.log(this.name);
    console.log(this.login);
    GithubRepos.loadDetails(this.name, this.login).subscribe(repos => {
      this.repos = repos;
      console.log("REPOS : " + repos);
    })
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ReposDetailsPage');
  }

}

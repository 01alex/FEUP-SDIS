import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Authentication } from '../../providers/authentication';
import { GithubUsers } from '../../providers/github-users/github-users';

@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class Profile {
  
  username: any;
  avatar: any;
  followers: any;
  following: any;
  public_repos: any;
  public_gists: any;

  repos: any;

  constructor(public navCtrl: NavController, public auth: Authentication, public user: GithubUsers) {

    user.loadDetails(this.auth.getUser().social.github.data.username).subscribe(
      userDetails => {
        this.username = userDetails.login;
        this.avatar = userDetails.avatar_url;
        this.followers = userDetails.followers;
        this.following = userDetails.following;
        this.public_repos = userDetails.public_repos;
        this.public_gists = userDetails.public_gists;
      },
      () => {
        console.log('getData completed');
      }
    );


    user.listUserRepositories(this.auth.getUser().social.github.data.username).subscribe(
      result => {
        this.repos = result;
      },
      () => {
        console.log('getData completed');
      }
    );
  }
}

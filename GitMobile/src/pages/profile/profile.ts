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

  constructor(public navCtrl: NavController, public auth: Authentication, public user: GithubUsers) {

    user.loadDetails(this.auth.getUser().social.github.data.username).subscribe(
      result => {
        this.username = result.login;
        this.avatar = result.avatar_url;
        this.followers = result.followers;
        this.following = result.following;
        this.public_repos = result.public_repos;
        this.public_gists = result.public_gists;
      },
      () => {
        console.log('getData completed');
      }
    );
  }
}

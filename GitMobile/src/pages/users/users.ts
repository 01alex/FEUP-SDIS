import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

import { User } from '../../models/users';
import { UserDetailsPage } from '../user-details/user-details';
import {  GithubUsers } from '../../providers/github-users/github-users';

@Component({
  selector: 'page-users',
  templateUrl: 'users.html'
})
export class UsersPage {
  users: User[]

  constructor(public navCtrl: NavController, private githubUsers: GithubUsers) {
    githubUsers.load().subscribe(users => {
      this.users = users;
    })
  }

  goToDetails(login: string) {
    this.navCtrl.push(UserDetailsPage, {login});
  }
}
import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

import { Repos } from '../../models/repos';
import { ReposDetailsPage } from '../repos-details/repos-details';


import {  GithubRepos } from '../../providers/github-repos';

@Component({
  selector: 'page-repos',
  templateUrl: 'repos.html'
})
export class ReposPage {
  repos: Repos[]

  constructor(public navCtrl: NavController, private githubRepos: GithubRepos) {
    githubRepos.listAllRepositories().subscribe(repos => {
      console.log("test");
      this.repos = repos;
      console.log(this.repos);
    })
  }

  goToDetails(name: string) {
    this.navCtrl.push(ReposDetailsPage, {name});
  }
}
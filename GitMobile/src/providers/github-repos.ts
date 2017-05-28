import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';

import { User } from '../models/users';
import { Repos } from '../models/repos';

@Injectable()
export class GithubRepos {
  githubApiUrl = 'https://api.github.com';


  constructor(public http: Http) { }

  listOwnRepositories(): Observable<Repos[]> {
    return this.http.get(`${this.githubApiUrl}/user/repos`) 
      .map(res => <Repos[]>(res.json().items))
  }

  listAllRepositories(): Observable<Repos[]> {
      console.log(this.http.get(`${this.githubApiUrl}/repositories`));
      
    return this.http.get(`${this.githubApiUrl}/repositories`) 
      .map(res => <Repos[]>res.json());
  }

  loadDetails(name: string, login: string): Observable<Repos> {
    return this.http.get(`${this.githubApiUrl}/repos/${login}/${name}`)
      .map(res => <Repos>(res.json()))
  }

 

}


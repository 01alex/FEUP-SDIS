import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';

import { MyApp } from './app.component';
import { HomePage } from '../pages/home/home';
import { Profile } from '../pages/profile/profile';

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

//Added modules
import { CloudSettings, CloudModule } from '@ionic/cloud-angular';
import { HttpModule } from '@angular/http';
import { Github } from '../providers/github';
import { Login } from '../pages/login/login';
import { Logout } from '../pages/logout/logout';
import { UsersPage } from '../pages/users/users';
import { ReposPage } from '../pages/repos/repos';
import { ReposDetailsPage } from '../pages/repos-details/repos-details';
import { UserDetailsPage } from '../pages/user-details/user-details';
import { GithubUsers } from '../providers/github-users/github-users';
import { GithubRepos } from '../providers/github-repos';



const cloudSettings: CloudSettings = {
  'core': {
    'app_id': '49da26d1'
  }
};

@NgModule({
  declarations: [
    MyApp,
    HomePage,
    Profile,
    Login,
    Logout,
    UsersPage,
    UserDetailsPage,
    ReposPage, 
    ReposDetailsPage
  ],
  imports: [
    BrowserModule,
    HttpModule,
    IonicModule.forRoot(MyApp),
    CloudModule.forRoot(cloudSettings)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    HomePage,
    Profile,
    Login,
    Logout,
    UsersPage,
    UserDetailsPage,
    ReposPage,
    ReposDetailsPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    GithubUsers,
    GithubRepos,
    Github,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class AppModule {}

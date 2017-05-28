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
import { UserDetailsPage } from '../pages/user-details/user-details';
import { GithubUsers } from '../providers/github-users/github-users';

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
    UserDetailsPage
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
    UserDetailsPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    GithubUsers,
    Github,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class AppModule {}

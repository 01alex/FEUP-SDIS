import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';

import { MyApp } from './app.component';
import { HomePage } from '../pages/home/home';
import { ListPage } from '../pages/list/list';

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

//Added modules
import { CloudSettings, CloudModule } from '@ionic/cloud-angular';
import { HttpModule } from '@angular/http';
import { Github } from '../providers/github';
import { Login } from '../pages/login/login';

const cloudSettings: CloudSettings = {
  'core': {
    'app_id': '49da26d1'
  }
};

@NgModule({
  declarations: [
    MyApp,
    HomePage,
    ListPage,
    Login
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
    ListPage,
    Login
  ],
  providers: [
    StatusBar,
    SplashScreen,
    Github,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class AppModule {}

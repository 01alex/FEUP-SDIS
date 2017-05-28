import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ReposDetailsPage } from './repos-details';

@NgModule({
  declarations: [
    ReposDetailsPage,
  ],
  imports: [
    IonicPageModule.forChild(ReposDetailsPage),
  ],
  exports: [
    ReposDetailsPage
  ]
})
export class ReposDetailsPageModule {}

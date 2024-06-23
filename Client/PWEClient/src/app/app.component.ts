import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';

import { ImageService } from './image.service';
import { ImageComponent } from './image/image.component';

@Component({
  selector: 'app-root',
  standalone : true,
  imports : [ImageComponent, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  providers: [ImageService],
})
export class AppComponent {
  title = 'Test';
  constructor(){
    console.log("AppComponent constructor executed");
  }
}

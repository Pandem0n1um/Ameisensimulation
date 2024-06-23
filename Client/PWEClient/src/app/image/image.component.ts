import { Subscription } from 'rxjs';

import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';

import { ImageService } from '../image.service';
import { SliderBarComponent } from '../slider-bar/slider-bar.component';

@Component({
    selector: 'app-image',
    standalone: true,
    templateUrl: './image.component.html',
    styleUrl: './image.component.css',
    providers: [HttpClientModule],
    imports: [CommonModule, SliderBarComponent],
})
export class ImageComponent implements OnInit{
  image?: String;
  imageSubscription!: Subscription;
  constructor(private imageService: ImageService, private ref: ChangeDetectorRef){
    console.log("ImageComponent constructor executed");
    ref.detach();
    this.image = "";
  }
  ngOnInit(): void
  {
    this.imageSubscription = this.imageService.imageUpate.subscribe({
      next: (newData: String) => {
        this.image = "data:image/png;base64," + newData;
        this.ref.detectChanges();
      },
    });
  }
}

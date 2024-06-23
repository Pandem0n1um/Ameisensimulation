import { BehaviorSubject } from 'rxjs';

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { ImageInterface } from './Interfaces/image-interface';

@Injectable({
  providedIn: 'root',
})
export class ImageService {
  image!: ImageInterface;
  public imageUpate: BehaviorSubject<string>;
  constructor(private httpClient: HttpClient) {
    console.log("ImageService constructor executed");
    this.imageUpate = new BehaviorSubject<string>("This is the dummy initialisation!!");
    let intervalId = setInterval(() => this.getImage(), 33);
  }

  async getImage()
  {
    const result = await Promise.resolve(this.httpClient.get<ImageInterface>('http://<your ip>:8000/image'));
    result.subscribe((stringValue) => {
      this.image = stringValue as ImageInterface;
      this.imageUpate.next(this.image.image);
    })
  }
}

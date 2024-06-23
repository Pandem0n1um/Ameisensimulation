import { debounceTime, Subject } from 'rxjs';

import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSliderModule } from '@angular/material/slider';

import { ParameterInterface } from '../Interfaces/parameter-interface';

@Component({
  selector: 'app-slider-bar',
  standalone: true,
  imports: [MatSliderModule, CommonModule, MatSlideToggleModule],
  templateUrl: './slider-bar.component.html',
  styleUrl: './slider-bar.component.css'
})
export class SliderBarComponent {
  names: string[] = ["Sensor Length", "Turn Left", "Turn Right", "Sensor Angle", "Fade Red", "Fade Green", "Fade Blue", "Toggle"];
  min: number[] = [1, 0, 0, 0, 0.8, 0.8, 0.8];
  max: number[] = [30, 0.5, 0.5, 1, 1, 1, 1];
  step: number[] = [1, 0.01, 0.01, 0.01, 0.001, 0.001, 0.001];
  values: number[] = [15, 0.25, 0.25, 0.2, 0.9, 0.9, 0.9];
  sensorLength:number = 15;
  turnLeft:number = 0.5;
  turnRight:number = 0.5;
  sensorAngle:number = 0.2;
  fadeRed:number = 0.9;
  fadeGreen:number = 0.9;
  fadeBlue:number = 0.9;
  subject: Subject<void> = new Subject<void>();
  value: number = 1;
  parameter: string = "Dummy";

  ngOnInit(): void {
    this.subject
        .pipe(debounceTime(200))
        .subscribe(() => {
          const body: ParameterInterface = {parameter: this.parameter, value: this.value};
          console.log(JSON.stringify(body));
          this.httpClient.post<ParameterInterface>('http://<your ip>:8000/parameter', JSON.stringify(body)).pipe(debounceTime(200)).subscribe(data => {
          console.log(data)});
            }
        );
}

  constructor(private ref: ChangeDetectorRef, private httpClient: HttpClient){}

  async updateSliderNotification(event: any, number: number)
  {
    this.value = number <= 6 ? event.target.valueAsNumber : +event.checked;
    this.parameter = this.names[number];
    this.ref.detectChanges()
    this.subject.next();
  }
}

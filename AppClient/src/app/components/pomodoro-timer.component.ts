import { Component, inject, OnInit } from '@angular/core';
import { SpotifyService } from '../services/SpotifyService';
import  Spotify from 'spotify-web-api-js';

@Component({
  selector: 'app-pomodoro-timer',
  standalone: false,
  templateUrl: './pomodoro-timer.component.html',
  styleUrl: './pomodoro-timer.component.css'
})
export class PomodoroTimerComponent {

  timeLeft: number = 25 * 60
  timer: any
  isRunning: boolean = false
  activeMode: string = 'focus'
  //alarmSound = new Audio('assets/alarm.mp3'); 


  // Pomodoro Timer Codes
  get formattedTime(): string {
    const minutes: number = Math.floor(this.timeLeft / 60);
    const seconds: number = this.timeLeft % 60;
    return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
  }

  startTimer(){
    if (!this.isRunning) {
      this.isRunning = true
      this.timer = setInterval(() => {
        if (this.timeLeft > 0) {
          this.timeLeft--
        } else {
          this.stopTimer()
          this.alertMsg(this.activeMode)
        }
      }, 1000)
    }
  }

  pauseTimer(){
    if (this.timer) {
      clearInterval(this.timer)
      this.timer = null
      this.stopTimer()
      this.isRunning = false
    }
  }

  resetTimer(){
    this.stopTimer()
    if(this.activeMode === 'focus'){
      this.timeLeft = 25 * 60
    } else if (this.activeMode === 'shortBreak') {
      this.timeLeft = 5 * 60
    } else if (this.activeMode === 'longBreak'){
      this.timeLeft = 30 * 60
    }
    this.isRunning = false

  }

  stopTimer() {
    this.isRunning = false;
    clearInterval(this.timer)
    //this.alarmSound
  }

  focus(){
    this.activeMode = 'focus'
    this.stopTimer()
    this.isRunning = false
    this.timeLeft = 25 * 60
  }

  shortBreak(){
    this.activeMode = 'shortBreak'
    this.stopTimer()
    this.isRunning = false
    this.timeLeft = 5 * 60
  }

  longBreak(){
    this.activeMode = 'longBreak'
    this.stopTimer()
    this.isRunning = false 
    this.timeLeft = 30 * 60
  }

  alertMsg(mode: string) {
    this.activeMode = mode
    if( mode == 'focus') {
      alert("Pomodoro session completed! Take a break. 😊")
    } else {
      alert("Break time is over! Time to focus. 😊")
    }
  }


}

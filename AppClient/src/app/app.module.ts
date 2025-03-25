import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login.component';
import { RouterModule, Routes } from '@angular/router';
import { HttpClient, provideHttpClient } from '@angular/common/http';
import { DashboardComponent } from './components/dashboard.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SignUpComponent } from './components/sign-up.component';
import { PomodoroTimerComponent } from './components/pomodoro-timer.component';
import { SpotifyService } from './services/SpotifyService';
import { MusicPlayerComponent } from './components/music-player.component';
import { RegistrationService } from './services/RegistrationService';
import { EmailverificationComponent } from './components/emailverification.component';
import { VerifiedComponent } from './components/verified.component';
import { WebSocketService } from './services/WebSocketService';
import { RxStompService } from '@stomp/ng2-stompjs';
import { LoginService } from './services/LoginService';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent},
  { path: 'pomodoro', component: PomodoroTimerComponent},
  { path: 'musicplayer', component: MusicPlayerComponent},
  { path: 'register', component: SignUpComponent },
  { path: 'verifyemail', component: EmailverificationComponent},
  { path: 'verify', component: VerifiedComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    SignUpComponent,
    PomodoroTimerComponent,
    MusicPlayerComponent,
    EmailverificationComponent,
    VerifiedComponent
  ],
  imports: [BrowserModule, ReactiveFormsModule, RouterModule.forRoot(routes), FormsModule], 
  providers:  [provideHttpClient(), SpotifyService, RegistrationService, WebSocketService, RxStompService, LoginService],
  bootstrap: [AppComponent]
})
export class AppModule { }

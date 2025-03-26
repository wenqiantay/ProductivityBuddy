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
import { EventCalendarComponent } from './components/event-calendar.component';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { EventService } from './services/EventService';
import { TodoService } from './services/TodoService';
import { UserStore } from './user.store';
import { AuthGuard } from './auth.guard';
import { IndexComponent } from './components/index.component';

const routes: Routes = [
  { path: '', component: IndexComponent},
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  { path: 'register', component: SignUpComponent },
  { path: 'verifyemail', component: EmailverificationComponent},
  { path: 'verify', component: VerifiedComponent},
  { path: '**', redirectTo: '', pathMatch: 'full'}
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
    VerifiedComponent,
    EventCalendarComponent,
    IndexComponent,
  ],
  imports: [BrowserModule, ReactiveFormsModule, RouterModule.forRoot(routes), FormsModule, MatIconModule, MatButtonModule, BrowserAnimationsModule], 
  providers:  [provideHttpClient(), SpotifyService, RegistrationService, WebSocketService, RxStompService, LoginService, EventService, TodoService, UserStore],
  bootstrap: [AppComponent]
})
export class AppModule { }

import { Component, inject } from '@angular/core';
import { FormControl } from '@angular/forms';
import { UserStore } from '../user.store';
import { TodoService } from '../services/TodoService';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent {

  protected todoList: string[] = [];
  input = new FormControl('');
  userStore = inject(UserStore);
  userId !: string
  private todoService = inject(TodoService);
  private router = inject(Router);

  protected username!: string
  todoList$ = this.userStore.todoList$;

  ngOnInit(): void {
    const savedUserData = localStorage.getItem('userData');
    if (savedUserData) {
      const userData = JSON.parse(savedUserData);
      this.userStore.setUserId(userData.userId);
      this.userStore.setSpotifyToken(userData.spotifyToken);
      this.userStore.setTodoList(userData.todoList || [])
    }
  
    this.userStore.userId$.subscribe(userId => {
      this.userId = userId || ''; 
      console.log("Logged in User ID:", this.userId); 
    });
   
    this.userStore.todoList$.subscribe(list => {
      this.todoList = list;
      localStorage.setItem('userData', JSON.stringify({
        ...JSON.parse(localStorage.getItem('userData') || '{}'),
        todoList: list
      }));
      console.log('Todo List:', this.todoList)
    });
  
    this.userStore.userName$.subscribe(userName => {
      this.username = userName || '';
      console.log("Logged in: ", userName);
    });
  }
  

  addTodo() {
    const task = this.input.value?.trim();
    if (task) {
      this.userStore.addTodo(task);
      this.input.setValue('');
      this.saveTodos();
    }
  }

  deleteTodo(idx: number) {
    this.userStore.deleteTodo(idx);
    this.saveTodos();
  }

  saveTodos() {
    this.userStore.todoList$.subscribe(todos => {
      this.todoService.saveTodos(this.userId, todos).subscribe({
        next: () => console.log('Todos saved!'),
        error: (err) => console.error('Failed to save todos', err)
      });
    }).unsubscribe();
  }

  handleClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (target.tagName === 'LI') {
      target.classList.toggle('checked');
    }
  }

  logout() {
   
    this.userStore.setUserId('');
    this.userStore.setUserName('');
    this.userStore.setEmail('');

    
    localStorage.removeItem('userData');


    this.router.navigate(['/login']);
  }
}


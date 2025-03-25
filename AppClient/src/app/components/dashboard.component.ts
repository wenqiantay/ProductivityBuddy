import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent {

  //Todo-list widget
  protected todoList: string[] = []
  input = new FormControl('')

  addTodo() {
   const task = this.input.value?.trim()
   if(task) {
    this.todoList.push(task)
    this.input.setValue('')
   }
  }

  deleteTodo(idx: number){
    this.todoList.splice(idx)
    
  }

  handleClick(event: MouseEvent) {
    const target = event.target as HTMLElement;

    if (target.tagName === 'LI') {
      target.classList.toggle('checked');
    }
  }

  

}


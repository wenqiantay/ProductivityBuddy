import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { map, Observable } from "rxjs";

@Injectable()
export class TodoService {

    private http = inject(HttpClient)

    getTodos(userId: string): Observable<string[]> {
        return this.http.get<any>(`/api/todos/${userId}`).pipe(
          map((response: { todos: any; }) => response.todos)
        );
      }
    
      saveTodos(userId: string, todos: string[]): Observable<any> {
        return this.http.post(`/api/todos/${userId}`, { todos });
      }
}

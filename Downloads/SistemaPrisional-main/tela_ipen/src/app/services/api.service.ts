import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/prisoners/list';

  constructor(private http: HttpClient) {}

  getPrisoners(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  registerVisit(visit: any) {
    return this.http.post('http://localhost:8080/visits', visit);
  }
}
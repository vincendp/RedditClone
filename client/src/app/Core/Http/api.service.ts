import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { HttpHeaders, HttpClient, HttpParams } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class ApiService {
  constructor(private http: HttpClient) {}

  get(path: string, params: HttpParams): Observable<any> {
    return this.http.get(`${environment.apiUrl}${path}`, { params }).pipe();
  }

  put(path: string, body: {}, options: {}): Observable<any> {
    return this.http
      .put(`${environment.apiUrl}${path}`, JSON.stringify(body), options)
      .pipe();
  }

  post(path: string, body: {}, options: {}): Observable<any> {
    return this.http
      .post(`${environment.apiUrl}${path}`, JSON.stringify(body), options)
      .pipe();
  }

  postFormData(path: string, body: {}, options: {}): Observable<any> {
    return this.http.post(`${environment.apiUrl}${path}`, body, options).pipe();
  }

  delete(path: string, options: {}): Observable<any> {
    return this.http.delete(`${environment.apiUrl}${path}`, options).pipe();
  }
}

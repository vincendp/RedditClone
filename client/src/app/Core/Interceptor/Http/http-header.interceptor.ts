import { Injectable } from "@angular/core";
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";

@Injectable()
export class HttpHeaderInterceptor implements HttpInterceptor {
  constructor() {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let headers = {};
    if (request.headers.keys().length <= 0) {
      headers["Content-Type"] = "application/json";
      headers["Accept"] = "application/json";
    }

    let req: HttpRequest<any>;
    req = request.clone({ setHeaders: headers, withCredentials: true });

    console.log(req);

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let message = "";
        if (error.error instanceof ErrorEvent) {
          message = "Error client: " + error.error.message;
        } else {
          message = "Error server: " + error.status + "    " + error.message;
        }

        alert(message);
        return throwError(message);
      })
    );
  }
}

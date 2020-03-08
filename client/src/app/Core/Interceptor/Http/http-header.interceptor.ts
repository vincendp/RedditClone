import { Injectable } from "@angular/core";
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable()
export class HttpHeaderInterceptor implements HttpInterceptor {
  constructor() {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const headers = {
      "Content-Type": "application/json",
      Accept: "application/json"
    };

    const req = request.clone({ setHeaders: headers, withCredentials: true });
    console.log(req);
    return next.handle(req);
  }
}

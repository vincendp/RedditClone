import { Injectable } from "@angular/core";
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from "@angular/common/http";
import { Observable } from "rxjs";

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

    return next.handle(req);
  }
}

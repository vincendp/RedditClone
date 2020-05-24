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
import { Router } from "@angular/router";
import { UserService } from "../../Services/user.service";

@Injectable()
export class HttpHeaderInterceptor implements HttpInterceptor {
  constructor(private router: Router, private userService: UserService) {}

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
          if (error.status == 403) {
            if (!req.url.endsWith("users")) {
              this.userService.setRedirectUrl(
                this.router.routerState.snapshot.url
              );
              this.router.navigateByUrl("/login");
              message = error.error.message;
            }
          } else {
            message = error.error.message;
          }
        }

        console.log(error);
        if (message.length > 0) alert(message);
        return throwError(error);
      })
    );
  }
}

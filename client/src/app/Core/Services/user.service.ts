import { Injectable } from "@angular/core";
import { Observable, BehaviorSubject } from "rxjs";
import { ApiService } from "../Http/api.service";
import { User } from "../Model/user.model";
import { Router, ActivatedRoute } from "@angular/router";

@Injectable({
  providedIn: "root"
})
export class UserService {
  private userSubject = new BehaviorSubject<User>({} as User);
  public user = this.userSubject.asObservable();

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated = this.isAuthenticatedSubject.asObservable();

  constructor(
    private apiService: ApiService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  login(credentials: {}) {
    this.apiService.post("/auth/login", credentials).subscribe(
      (data: {}) => {
        console.log(data);
        this.userSubject.next(<User>data["result"]);
        this.isAuthenticatedSubject.next(true);

        this.router.navigateByUrl("/");
      },
      err => {
        console.log(err);
      }
    );
  }

  logout() {
    this.apiService.post("/logout", null).subscribe(
      (data: {}) => {
        console.log(data);
        this.userSubject.next({} as User);
        this.isAuthenticatedSubject.next(false);
        this.router.navigateByUrl("/Login");
      },
      err => {
        console.log(err);
      }
    );
  }

  signup(credentials: {}) {
    this.apiService.post("/users", credentials).subscribe(
      (data: {}) => {
        console.log(data);

        this.userSubject.next(<User>data["result"]);
        this.isAuthenticatedSubject.next(true);

        this.router.navigateByUrl("/");
      },
      err => {
        console.log(err);
      }
    );
  }
}

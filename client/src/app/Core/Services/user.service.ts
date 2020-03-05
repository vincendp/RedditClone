import { Injectable } from "@angular/core";
import { Observable, BehaviorSubject } from "rxjs";
import { ApiService } from "../Http/api.service";
import { User } from "../Model/user.model";

@Injectable({
  providedIn: "root"
})
export class UserService {
  private userSubject = new BehaviorSubject<User>({} as User);
  public user = this.userSubject.asObservable();

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(true);
  public isAuthenticated = this.isAuthenticatedSubject.asObservable();

  constructor(private apiService: ApiService) {}

  login(credentials: {}) {
    // api call

    this.apiService.post("/users", credentials).subscribe(() => {
      this.isAuthenticatedSubject.next(true);
    });
  }
}

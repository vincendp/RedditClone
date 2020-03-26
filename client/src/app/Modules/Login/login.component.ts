import { Component, OnInit } from "@angular/core";
import { FormBuilder, Validators } from "@angular/forms";
import { UserService } from "src/app/Core/Services/user.service";
import { ApiService } from "src/app/Core/Http/api.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"]
})
export class LoginComponent implements OnInit {
  userLogin: any;
  submitted: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.userLogin = this.formBuilder.group({
      username: ["", Validators.required],
      password: ["", Validators.required]
    });
  }

  login() {
    this.submitted = true;
    this.userService.login(this.userLogin.value);
    console.log(this.userLogin.value);
  }

  callRestrictedAPI() {
    this.apiService.get("/helloWorld", null).subscribe(
      data => {
        console.log(data);
      },
      err => {
        console.log(err);
      }
    );
  }

  callRestrictedAPI2() {
    this.apiService.post("/logout", null).subscribe(
      data => {
        console.log(data);
      },
      err => {
        console.log(err);
      }
    );
  }
}

import { Component, OnInit } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { UserService } from "src/app/Core/Services/user.service";
import { ApiService } from "src/app/Core/Http/api.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
})
export class LoginComponent implements OnInit {
  userLogin: FormGroup;
  submitted: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.userLogin = this.formBuilder.group({
      username: ["", Validators.required],
      password: ["", Validators.required],
    });
  }

  login() {
    this.submitted = true;
    this.userService.login(this.userLogin.value);
  }
}

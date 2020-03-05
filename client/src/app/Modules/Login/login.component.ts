import { Component, OnInit } from "@angular/core";
import { FormBuilder, Validators } from "@angular/forms";
import { UserService } from "src/app/Core/Services/user.service";

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
    private userService: UserService
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
}

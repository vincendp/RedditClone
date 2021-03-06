import { Component, OnInit } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { UserService } from "src/app/Core/Services/user.service";
import { ApiService } from "src/app/Core/Http/api.service";

@Component({
  selector: "app-signup",
  templateUrl: "./signup.component.html",
  styleUrls: ["./signup.component.scss"],
})
export class SignupComponent implements OnInit {
  userSignup: FormGroup;
  submitted: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.userSignup = this.formBuilder.group({
      username: ["", Validators.required],
      password: ["", Validators.required],
      verifyPassword: ["", Validators.required],
    });
  }

  signup() {
    this.userService.signup(this.userSignup.value);
  }
}

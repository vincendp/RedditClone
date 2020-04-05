import { Component, OnInit } from "@angular/core";
import { UserService } from "src/app/Core/Services/user.service";
import { ApiService } from "src/app/Core/Http/api.service";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"]
})
export class HomeComponent implements OnInit {
  constructor(
    private userService: UserService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {}

  createPost() {
    this.apiService
      .post("/posts", {
        title: "Test Post",
        description: "HELLO WOORLD",
        link: "ASDAFAFAF",
        user_id: "333",
        subreddit_id: "333"
      })
      .subscribe(
        (data: {}) => {
          console.log(data);
        },
        err => {
          console.log(err);
        }
      );
  }
}

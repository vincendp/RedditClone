import { Component, OnInit } from "@angular/core";
import { UserService } from "../../Services/user.service";

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"]
})
export class HeaderComponent implements OnInit {
  is_logged_in: boolean;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.isAuthenticated.subscribe(
      data => {
        console.log(data);
        this.is_logged_in = data;
      },
      err => {
        console.log(err);
      }
    );
  }

  logout() {
    this.userService.logout();
  }
}

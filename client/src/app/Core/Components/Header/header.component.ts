import { Component, OnInit, ViewChild, ElementRef } from "@angular/core";
import { UserService } from "../../Services/user.service";
import { UtilityService } from "../../Services/utility.service";

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"],
})
export class HeaderComponent implements OnInit {
  isLoggedIn: boolean;
  dropdownIsActive: boolean;

  @ViewChild("dropdownButton", { read: ElementRef, static: false })
  dropdownButton: ElementRef;

  constructor(
    private userService: UserService,
    private utilityService: UtilityService
  ) {}

  ngOnInit(): void {
    this.userService.populate();

    this.userService.isAuthenticated.subscribe(
      (data) => {
        this.isLoggedIn = data;
      },
      (err) => {
        console.log(err);
      }
    );

    this.utilityService.documentClickTarget.subscribe(
      (data) => {
        this.onDocumentClick(data);
      },
      (err) => {
        console.log(err);
      }
    );
  }

  logout(): void {
    this.userService.logout();
  }

  onToggleDropdown(): void {
    this.dropdownIsActive = !this.dropdownIsActive;
  }

  onDocumentClick(target: any): void {
    if (!this.dropdownButton.nativeElement.contains(target)) {
      this.dropdownIsActive = false;
    }
  }
}

import { Component, HostListener } from "@angular/core";
import { UtilityService } from "./Core/Services/utility.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
})
export class AppComponent {
  title = "RedditClone";

  constructor(private utilityService: UtilityService) {}

  @HostListener("document:click", ["$event"])
  documentClick(event: any): void {
    this.utilityService.setDocumentClickTarget(event.target);
  }
}

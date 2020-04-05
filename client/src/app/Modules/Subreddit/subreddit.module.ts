import { NgModule } from "@angular/core";
import { SharedModule } from "src/app/Shared/shared.module";
import { CoreModule } from "src/app/Core/core.module";
import { SubredditRoutingModule } from "./subreddit-routing.module";
import { SubredditComponent } from "./subreddit.component";

@NgModule({
  declarations: [SubredditComponent],
  imports: [SharedModule, CoreModule, SubredditRoutingModule],
})
export class SubredditModule {}

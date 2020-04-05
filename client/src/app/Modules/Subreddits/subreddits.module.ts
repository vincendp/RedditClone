import { NgModule } from "@angular/core";
import { SharedModule } from "src/app/Shared/shared.module";
import { CoreModule } from "src/app/Core/core.module";
import { SubredditsRoutingModule } from "./subreddits-routing.module";
import { SubredditsComponent } from "./subreddits.component";

@NgModule({
  declarations: [SubredditsComponent],
  imports: [SharedModule, CoreModule, SubredditsRoutingModule],
})
export class SubredditsModule {}

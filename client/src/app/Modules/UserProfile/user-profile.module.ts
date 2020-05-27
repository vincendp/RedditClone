import { NgModule } from "@angular/core";
import { SharedModule } from "src/app/Shared/shared.module";
import { CoreModule } from "src/app/Core/core.module";
import { UserProfileRoutingModule } from "./user-profile-routing.module";
import { UserProfileComponent } from "./user-profile.component";

@NgModule({
  declarations: [UserProfileComponent],
  imports: [SharedModule, CoreModule, UserProfileRoutingModule],
})
export class UserProfileModule {}

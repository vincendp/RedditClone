import { NgModule } from "@angular/core";
import { SharedModule } from "src/app/Shared/shared.module";
import { CoreModule } from "src/app/Core/core.module";
import { SignupRoutingModule } from "./signup-routing.module";
import { SignupComponent } from "./signup.component";

@NgModule({
  declarations: [SignupComponent],
  imports: [SharedModule, CoreModule, SignupRoutingModule]
})
export class SignupModule {}

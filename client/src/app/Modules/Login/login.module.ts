import { NgModule } from "@angular/core";
import { LoginComponent } from "./login.component";
import { SharedModule } from "src/app/Shared/shared.module";
import { LoginRoutingModule } from "./login-routing.module";
import { CoreModule } from "src/app/Core/core.module";

@NgModule({
  declarations: [LoginComponent],
  imports: [SharedModule, LoginRoutingModule, CoreModule]
})
export class LoginModule {}

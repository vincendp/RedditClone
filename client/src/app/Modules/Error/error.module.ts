import { NgModule } from "@angular/core";
import { SharedModule } from "src/app/Shared/shared.module";
import { ErrorRoutingModule } from "./error-routing.module";
import { CoreModule } from "src/app/Core/core.module";
import { NotFoundComponent } from './not-found.component';

@NgModule({
  declarations: [NotFoundComponent],
  imports: [SharedModule, ErrorRoutingModule, CoreModule],
})
export class ErrorModule {}

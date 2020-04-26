import { NgModule } from "@angular/core";
import { SharedModule } from "src/app/Shared/shared.module";
import { CoreModule } from "src/app/Core/core.module";
import { PostRoutingModule } from "./post-routing.module";
import { PostComponent } from "./post.component";

@NgModule({
  declarations: [PostComponent],
  imports: [SharedModule, CoreModule, PostRoutingModule],
})
export class PostModule {}

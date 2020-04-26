import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HeaderComponent } from "./Components/Header/header.component";
import { PostsComponent } from "./Components/Posts/posts.component";
import { SvgIconComponent } from "./Components/SVGIcon/svg-icon.component";
import { LazyLoadDirective } from "./Directives/lazy-load.directive";

@NgModule({
  declarations: [
    HeaderComponent,
    PostsComponent,
    SvgIconComponent,
    LazyLoadDirective,
  ],
  imports: [CommonModule, RouterModule, FormsModule, ReactiveFormsModule],
  exports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    HeaderComponent,
    PostsComponent,
    SvgIconComponent,
    LazyLoadDirective,
  ],
})
export class SharedModule {}

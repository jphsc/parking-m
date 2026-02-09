import { AfterViewChecked, Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-nao-encontrada',
  templateUrl: './nao-encontrada.component.html',
  styleUrls: ['./nao-encontrada.component.css']
})
export class NaoEncontradaComponent implements AfterViewChecked {

  protected img_not_found: string = 'assets/images/image-not-found.png';

  constructor(private rota:Router, private ar:ActivatedRoute){}

  ngAfterViewChecked(): void {
    console.log(this.rota.url);
  }

}

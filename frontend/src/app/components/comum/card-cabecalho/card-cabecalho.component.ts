import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-card-cabecalho',
  templateUrl: './card-cabecalho.component.html',
  styleUrls: ['./card-cabecalho.component.css']
})
export class CardCabecalhoComponent {
  @Input() titulo: string = "";
}

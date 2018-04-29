import * as React from "react";
import { withRouter, Route } from "react-router-dom";
import { Layout } from "./components/Layout";
import { MainComponent } from "./components/MainComponent";
import { AdminComponent } from "./components/AdminComponent";
import { FetchService } from "./services/FetchService";

import 'es6-promise/auto';

let fetchService = new FetchService();

export const RoutedLayout = withRouter((props) => <Layout {...props} fetchService={fetchService} />);

export const routes = <RoutedLayout>
							
                      </RoutedLayout>;